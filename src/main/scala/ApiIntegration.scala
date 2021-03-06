/*
 * Copyright 2020 hot.crew
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

package hot.crew.s3

import zio.{ Runtime, ZLayer }
import hot.crew.s3.awsLink.AwsLink

object App0 extends App {

  val rt = Runtime.default

  val key    = sys.env("AWS_ACCESS_KEY_ID")
  val secret = sys.env("AWS_SECRET_ACCESS_KEY")

  val creds = S3Credentials(key, secret)

  val region   = software.amazon.awssdk.regions.Region.EU_CENTRAL_1
  val endpoint = Option("http://localhost:9000")
  val bucket   = "icare/mybuck"

  val s3  = AwsAgent.liveLayer(region, creds, endpoint)
  val env = s3 >>> AwsLink.live

  val prog = awsLink.listBucketObjects(bucket, "").provideCustomLayer(env)

  rt.unsafeRun(prog)

}
