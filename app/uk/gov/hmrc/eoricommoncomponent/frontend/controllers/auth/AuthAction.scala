/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.eoricommoncomponent.frontend.controllers.auth

import javax.inject.Inject
import play.api.{Configuration, Environment}
import play.api.mvc._
import uk.gov.hmrc.auth.core.AuthProvider.GovernmentGateway
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals.{affinityGroup, allEnrolments, internalId, email => ggEmail, _}
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.eoricommoncomponent.frontend.domain._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class AuthAction @Inject() (
  override val config: Configuration,
  override val env: Environment,
  override val authConnector: AuthConnector
)(implicit ec: ExecutionContext)
    extends AuthRedirectSupport with AuthorisedFunctions with AccessController {

  private type RequestProcessorSimple =
    Request[AnyContent] => LoggedInUserWithEnrolments => Future[Result]

  private type RequestProcessorExtended =
    Request[AnyContent] => Option[String] => LoggedInUserWithEnrolments => Future[Result]

  private val baseRetrievals     = ggEmail and credentialRole and affinityGroup
  private val extendedRetrievals = baseRetrievals and internalId and allEnrolments and groupIdentifier

  def ggAuthorisedUserWithEnrolmentsAction(requestProcessor: RequestProcessorSimple) =
    Action.async { implicit request =>
      implicit val hc: HeaderCarrier =
        HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

      authorised(AuthProviders(GovernmentGateway))
        .retrieve(extendedRetrievals) {
          case currentUserEmail ~ userCredentialRole ~ userAffinityGroup ~ userInternalId ~ userAllEnrolments ~ groupId =>
            transformRequest(
              Right(requestProcessor),
              userAffinityGroup,
              userInternalId,
              userAllEnrolments,
              currentUserEmail,
              userCredentialRole,
              groupId
            )
        } recover withAuthRecovery(request)
    }

  private def transformRequest(
    requestProcessor: Either[RequestProcessorExtended, RequestProcessorSimple],
    userAffinityGroup: Option[AffinityGroup],
    userInternalId: Option[String],
    userAllEnrolments: Enrolments,
    currentUserEmail: Option[String],
    userCredentialRole: Option[CredentialRole],
    groupId: Option[String]
  )(implicit request: Request[AnyContent]) = {
    val loggedInUser =
      LoggedInUserWithEnrolments(userAffinityGroup, userInternalId, userAllEnrolments, currentUserEmail, groupId)

    permitUserOrRedirect(userAffinityGroup, userCredentialRole, currentUserEmail) {
      requestProcessor fold (_(request)(userInternalId)(loggedInUser), _(request)(loggedInUser))
    }
  }

}