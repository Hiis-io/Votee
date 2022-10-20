package votee

import play.api.libs.json.*
import spire.math.Rational
import votee.models.*
import votee.utils.Parser

import scala.util.{Failure, Success, Try}

/**
 * Created by Abanda Ludovic on 20/10/2022
 */
trait UnitSpec {
  import UnitSpec.preferentialCandidateFormat
  import UnitSpec.PreferentialBallotReads

  def candidatesAndBallots(candidatesFile: String, ballotFile: String): (Seq[PreferentialCandidate], Seq[PreferentialBallot[PreferentialCandidate]]) =
    (Parser.parseCandidates("src/main/resources/" + candidatesFile), Parser.parseBallot[PreferentialCandidate, PreferentialBallot]("src/main/resources/" + ballotFile))
}

object UnitSpec {
  given PreferentialBallotReads[A <: Candidate](using Reads[A]): Reads[PreferentialBallot[A]] = {
    case ballot: JsObject =>
      Try {
        val id = (ballot \ "id").as[Int]
        val weight = (ballot \ "weight").as[Double]
        val preferences = (ballot \ "preferences").as[List[A]]

        JsSuccess(PreferentialBallot(id, Rational(weight), preferences))
      } match {
        case Success(value) => value
        case Failure(exception) => JsError(exception.getMessage)
      }
    case e => JsError("expected.jsObject")
  }

  given PreferentialBallotWrites[A <: Candidate](using OWrites[A]): OWrites[PreferentialBallot[A]] = (o: PreferentialBallot[A]) => Json.obj(
    "id" -> o.id,
    "weight" -> o.weight.toDouble,
    "preferences" -> o.preferences
  )

  given preferentialCandidateFormat: OFormat[PreferentialCandidate] = Json.format[PreferentialCandidate]

}