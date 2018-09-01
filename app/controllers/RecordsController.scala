package controllers

import javax.inject._

import models.entities.Record
import models.repos.RecordRepository
import play.api.libs.json.{Json, Writes}
import play.api.mvc._
import util.DBImplicits

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

@Singleton
class RecordsController @Inject()(recordsDAO: RecordRepository, dbExecuter: DBImplicits) extends Controller {

  import dbExecuter.executeOperation

  implicit val recordWrites = new Writes[Record] {
    def writes(sup: Record) = Json.obj(
      "pk" -> sup.pk,
      "score" -> sup.score,
    )
  }

  def record(pk: String) = Action.async {
    recordsDAO.findOne(pk) map { sup => sup.fold(NoContent)(sup => Ok(Json.toJson(sup))) }
  }


  def insertRecord = Action.async(parse.json) {
    request => {
      for {
        pk <- (request.body \ "pk").asOpt[String]
        score <- (request.body \ "score").asOpt[String]
      } yield {
        recordsDAO.save(Record(pk, score)).mapTo[Record] map {
          sup => Created("Id of Supplier Added : " + sup.pk)
        } recoverWith {
          case e => Future {
            println(e)
            InternalServerError("Something wrong on server")
          }

        }
      }
    }.getOrElse(Future {
      BadRequest("Wrong json format")
    })
  }

}
