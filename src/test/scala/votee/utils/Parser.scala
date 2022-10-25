package votee.utils

import play.api.libs.json.{ JsError, JsSuccess, Json, Reads }
import votee.models.{ Ballot, Candidate }

/** Created by Abanda Ludovic on 27/04/2022. */

case object Parser:
  def parseCandidates[C <: Candidate](jsonFilePath: String)(using Reads[C]): Seq[C] =
    val source             = io.Source.fromFile(jsonFilePath)
    val jsonString: String = source.getLines.mkString.trim.replaceAll("\\s", "")
    source.close()

    Json.parse(jsonString).asOpt[Seq[C]].getOrElse(Seq.empty)
  end parseCandidates

  def parseBallot[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](jsonFilePath: String)(
      using Reads[B[C]]
  ): Seq[B[C]] =
    val source             = io.Source.fromFile(jsonFilePath)
    val jsonString: String = source.getLines.mkString.trim.replaceAll("\\s", "")
    source.close()

    Json.parse(jsonString).asOpt[Seq[B[C]]].getOrElse(Seq.empty[B[C]])
  end parseBallot

end Parser
