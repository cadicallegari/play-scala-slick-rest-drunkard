package models.entities

import com.byteslounge.slickrepo.meta.{Entity}

//case class Record(override val id: Option[Int], pk: String, score: String) extends Entity[Record, Int]{
//  def withId(id: Int): Record = this.copy(id = Some(id))
//}

case class Record(pk: String, score: String) extends Entity[Record, String] {
  override val id: Option[String] = {
    Option(pk)
  }

  override def withId(id: String): Record = {
    this.copy(id)
  }
}
