package io.hiis.votee.utils

import io.hiis.votee.models.{ Ballot, Candidate }
import play.api.libs.json.{ JsError, JsSuccess, Json, Reads }

/** Created by Abanda Ludovic on 27/04/2022. */

case object Parser:
  def parseCandidates[C <: Candidate](jsonFilePath: String)(using Reads[C]): Seq[C] =
    val source             = scala.io.Source.fromFile(jsonFilePath)
    val jsonString: String = source.getLines.mkString.trim.replaceAll("\\s", "")
    source.close()

    Json.parse(jsonString).asOpt[Seq[C]].getOrElse(Seq.empty)
  end parseCandidates

  def parseBallot[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](jsonFilePath: String)(
      using Reads[B[C]]
  ): Seq[B[C]] =
    val source             = scala.io.Source.fromFile(jsonFilePath)
    val jsonString: String = source.getLines.mkString.trim.replaceAll("\\s", "")
    source.close()

    Json.parse(jsonString).asOpt[Seq[B[C]]].getOrElse(Seq.empty[B[C]])
  end parseBallot

end Parser
