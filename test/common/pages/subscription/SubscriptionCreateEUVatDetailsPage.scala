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

package common.pages.subscription

import common.pages.WebPage
import common.support.Env

trait SubscriptionCreateEUVatDetailsPage extends WebPage {

  val url: String = Env.frontendHost + "/customs-enrolment-services/register-for-cds/vat-registered-eu"
}

object SubscriptionCreateEUVatDetailsPage extends SubscriptionCreateEUVatDetailsPage {
  override val title = "Is your organisation VAT registered in other EU member countries?"
}

object SoleTraderEuVatDetailsPage extends SubscriptionCreateEUVatDetailsPage {
  override val title = "Are you VAT registered in other EU member countries?"
}
