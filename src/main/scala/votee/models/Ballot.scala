package votee.models

import jdk.nashorn.api.scripting.JSObject
import play.api.libs.json.{JsError, JsObject, JsResult, JsSuccess, JsValue, Json, OFormat, OWrites, Reads}
import spire.math.Rational

import scala.util.{Failure, Success, Try}

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait BallotOps[C <: Candidate, B <: Ballot[C]]:
  def excludeCandidates(candidates: List[C]): B
  def includeCandidates(candidates: List[C]): B
end BallotOps

abstract class Ballot[C <: Candidate](val id: Int, val weight: Rational, val preferences: List[C]) extends BallotOps[C, Ballot[C]]:
  require(preferences.nonEmpty)
end Ballot

final case class PreferentialBallot[C <: Candidate](override val id: Int, override val weight: Rational = Rational(1, 1), override val preferences: List[C])
  extends Ballot[C](id, weight, preferences):
  override def excludeCandidates(candidates: List[C]): PreferentialBallot[C] = PreferentialBallot(id, weight, preferences.filterNot(candidates.contains(_)))
  override def includeCandidates(candidates: List[C]): PreferentialBallot[C] = PreferentialBallot(id, weight, preferences ++ candidates)
end PreferentialBallot

object PreferentialBallot {
  given PreferentialBallotReads[A <: Candidate](using OFormat[A]): Reads[PreferentialBallot[A]] = {
    case ballot: JsObject =>
      Try {
        val id = (ballot \ "id").as[Int]
        val weight = (ballot \ "weight").as[Double]
        val preferences = (ballot \ "preferences").as[List[A]]

        JsSuccess(new PreferentialBallot[A](id, Rational(weight), preferences))
      } match {
        case Success(value) => value
        case Failure(exception) => JsError(exception.getMessage)
      }
    case e => JsError("expected.jsObject")
  }

  given PreferentialBallotWrites[A <: Candidate](using OFormat[A]): OWrites[PreferentialBallot[A]] = (o: PreferentialBallot[A]) => Json.obj(
    "id" -> o.id,
    "weight" -> o.weight.toDouble,
    "preferences" -> o.preferences
  )

  }
