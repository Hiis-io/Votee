package votee.algorithms

import votee.models.{Candidate, Election, PreferenceBallot, PreferentialCandidate, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Approval_voting
 */

trait Approval[C <: Candidate, B <: PreferenceBallot[C]] extends Election[C, B, Winner[C]]:
  override def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    for (ballot <- ballots)
      for(candidate <- ballot.preferences)
        candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))

    candidateScoreMap.toList.sortWith( _._2 > _._2 ).take(vacancies).map(Winner(_))
  end run
end Approval

case object Approval extends Approval[PreferentialCandidate, PreferenceBallot[PreferentialCandidate]]