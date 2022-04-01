package votee.models

import votee.utils.Rational

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

abstract class Ballot[C](val id: Int, val weight: Rational):
  def includeCandidates(candidates: List[C]): Ballot[C]
  def excludeCandidates(candidates: List[C]): Ballot[C]

class PreferenceBallot[C <: Candidate](override val id: Int, override val weight: Rational = Rational(1, 1), val preferences: List[C])
  extends Ballot[C](id, weight):
  require(preferences.nonEmpty)
  lazy val firstVotes: Map[_ <: C, Rational] = preferences.headOption match {
    case Some(c) => Map(c -> Rational(1,1))
    case None => Map()
  }

  def includeCandidates(candidates: List[C]): PreferenceBallot[C] = PreferenceBallot(id, weight, preferences ++ candidates)

  def excludeCandidates(candidates: List[C]): PreferenceBallot[C] = PreferenceBallot(id, weight, preferences.filterNot(candidates.contains(_)))
end PreferenceBallot
