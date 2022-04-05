package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Approval_voting
 */

trait Approval[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override def run[CC <: C, BB <: B](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    for (ballot <- ballots)
      for(candidate <- ballot.preferences)
        candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))

    candidateScoreMap.toList.sortWith( _._2 > _._2 ).take(vacancies).map(Winner(_))
  end run
end Approval

case object Approval extends Approval[PreferentialCandidate, PreferentialBallot[PreferentialCandidate]]