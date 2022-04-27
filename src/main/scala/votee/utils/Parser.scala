package votee.utils

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{JsError, JsSuccess, Json, OFormat, Reads}
import votee.models.{Ballot, Candidate}

/**
 * Created by Abanda Ludovic on 27/04/2022.
 */

case object Parser extends LazyLogging:
  def parseCandidates[C <: Candidate](jsonFilePath: String)(using Reads[C]): List[C] =
    val source = io.Source.fromFile(jsonFilePath)
    val jsonString: String = source.getLines.mkString.trim.replaceAll("\\s", "")
    source.close()

    val candidates: List[C] = Json.parse(jsonString).validate[List[C]] match
      case s: JsSuccess[List[C]] => s.get
      case JsError(errors) =>
        logger.error(s"Error parsing candidate file($jsonFilePath): ${errors}")
        List.empty
    candidates
  end parseCandidates

  def parseBallot[C <: Candidate, B <: Ballot[C]](jsonFilePath: String)(using Reads[B]): List[B] =
    val source = io.Source.fromFile(jsonFilePath)
    val jsonString: String = source.getLines.mkString.trim.replaceAll("\\s", "")
    source.close()

    val ballots: List[B] = Json.parse(jsonString).validate[List[B]] match
      case s: JsSuccess[List[B]] => s.get
      case JsError(errors) =>
        logger.error(s"Error parsing ballot file($jsonFilePath): ${errors}")
        List.empty
    ballots
  end parseBallot

end Parser
