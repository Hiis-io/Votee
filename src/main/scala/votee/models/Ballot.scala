package votee.models

import votee.utils.Rational

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait BallotOps[C <: Candidate]:
  type T <: Ballot[C]
  lazy val firstVotes: Map[C, Rational]
  def includeCandidates(candidates: List[C]): T
  def excludeCandidates(candidates: List[C]): T

abstract class Ballot[C <: Candidate](val id: Int, val weight: Rational, val preferences: List[C]) extends BallotOps[C]

final case class PreferentialBallot[C <: Candidate](override val id: Int, override val weight: Rational = Rational(1, 1), override val preferences: List[C])
  extends Ballot[C](id, weight, preferences):
  override type T = PreferentialBallot[C]
  require(preferences.nonEmpty)
  lazy val firstVotes: Map[C, Rational] = preferences.headOption match {
    case Some(c) => Map(c -> Rational(1,1))
    case None => Map()
  }

  override def includeCandidates(candidates: List[C]): T = PreferentialBallot(id, weight, preferences ++ candidates)

  override def excludeCandidates(candidates: List[C]): T = PreferentialBallot(id, weight, preferences.filterNot(candidates.contains(_)))
end PreferentialBallot
