import java.time.Clock

import com.google.inject.{AbstractModule, Provides}
import models.entities.Record
import models.repos.RecordRepository
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test._
import org.specs2.execute.Results
import org.specs2.matcher.Matchers
import org.specs2.mock.Mockito
import play.api.libs.json.{JsObject, JsString, Json}
import slick.dbio.DBIOAction
import play.api.libs.concurrent.Execution.Implicits._
import slick.SlickException

import scala.concurrent.{ExecutionContext, Future}

class ApplicationSpec extends PlaySpecification with Results with Matchers with Mockito {
  sequential

  val daoMock = mock[RecordRepository]

  val app = new GuiceApplicationBuilder().overrides(new AbstractModule {
    override def configure() = {
      bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    }

    @Provides
    def recordsDAO: RecordRepository = daoMock
  }).build

  "Routes" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) shouldEqual Some(NOT_FOUND)
    }

    "send 204 when there isn't a /records/1" in {
      daoMock.findOne("0").returns(DBIOAction.from(Future {
        None
      }))
      route(app, FakeRequest(GET, "/records/0")).map(
        status(_)) shouldEqual Some(NO_CONTENT)
    }

    "send 200 when there is a /records/1" in {
      daoMock.findOne("123").returns(DBIOAction.from(Future {
        Some(Record("123", "50"))
      }))
      route(app, FakeRequest(GET, "/records/123")).map(
        status(_)) shouldEqual Some(OK)
    }

    "send 415 when post to create a record without json type" in {
      route(app, FakeRequest(POST, "/records")).map(
        status(_)) shouldEqual Some(UNSUPPORTED_MEDIA_TYPE)
    }

    "send 400 when post to create a record with empty json" in {
      route(app,
        FakeRequest(POST, "/records", FakeHeaders(("Content-type", "application/json") :: Nil), JsObject(Seq()))).map(
        status(_)) shouldEqual Some(BAD_REQUEST)
    }

    "send 400 when post to create a record with wrong json" in {
      route(app,
        FakeRequest(POST, "/records", FakeHeaders(("Content-type", "application/json") :: Nil), JsObject(Seq("wrong" -> JsString("wrong"))))).map(
        status(_)) shouldEqual Some(BAD_REQUEST)
    }

    "send 201 when post to create a record with valid json" in {
      val (pk, score) = ("216654", "10")
      val record = Record(pk, score)
      daoMock.save(record).returns(DBIOAction.from(Future {
        record.copy(pk = "1")
      }))

      val body = Json.parse(s"""{"pk": "${pk}", "score": "${score}"}""")
      val request = FakeRequest(POST, "/records")
        .withJsonBody(body)

      val Some(response) = route(app, request)

      status(response) shouldEqual (CREATED)

    }


  }

}

