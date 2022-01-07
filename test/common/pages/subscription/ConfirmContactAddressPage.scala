/*
 * Copyright 2022 HM Revenue & Customs
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

package common.pages.subscription

import common.pages.WebPage
import uk.gov.hmrc.eoricommoncomponent.frontend.forms.models.subscription.ContactAddressModel

sealed trait ConfirmContactAddressPage extends WebPage {

  val formId: String = "confirmContactAddressForm"

  val continueButtonXpath                = "//*[@class='govuk-button']"
  override val title: String             = "Is this your contact address?"
  val hintTextXpath                      = "//*[@id='address-hint']"
  val addressTextXpath                   = "//*[@id='address']"
  val fieldLevelErrorYesNoAnswer: String = "//span[@id='yes-no-answer-error'][@class='govuk-error-message']"
  val PageLevelErrorSummaryListXPath     = "//ul[@class='govuk-list govuk-error-summary__list']"

  val filledValues =
    ContactAddressModel(
      lineOne = "Line 1",
      lineTwo = Some("Line 2"),
      lineThree = "Town",
      lineFour = Some("Region"),
      postcode = Some("SE28 1AA"),
      country = "GB"
    )

}

object ConfirmContactAddressPage extends ConfirmContactAddressPage
