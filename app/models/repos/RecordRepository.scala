package models.repos

import com.byteslounge.slickrepo.meta.Keyed
import com.byteslounge.slickrepo.repository.Repository
import com.google.inject.Inject
import models.entities.Record
import play.api.db.slick.DatabaseConfigProvider
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile

class RecordRepository @Inject()(dbConfigProvider: DatabaseConfigProvider) extends Repository[Record, String](dbConfigProvider.get[JdbcProfile].profile) {

  import driver.api._

  val pkType = implicitly[BaseTypedType[String]]
  val tableQuery = TableQuery[Records]
  type TableType = Records

  class Records(tag: Tag) extends Table[Record](tag, "records") with Keyed[String] {
    override def id = pk

    def pk = column[String]("pk", O.PrimaryKey)

    def score = column[String]("score")

    def * = (pk, score) <> (Record.tupled, Record.unapply)
  }

}
