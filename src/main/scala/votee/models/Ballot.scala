package votee.models

import votee.utils.Rational

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