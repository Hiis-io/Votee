package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Approval_voting
 */

sealed trait Approval[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    for (ballot <- ballots)
      for(candidate <- ballot.preferences)
        candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))

    candidateScoreMap.toList.sortWith( _._2 > _._2 ).take(vacancies).map(Winner(_))
  end run
end Approval

object Approval:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int)(tieResolver: TieResolver[CC]): List[Winner[CC]] =
    new Approval[CC, BB]{}.run(ballots, candidates, vacancies)(tieResolver)

  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] = new Approval[CC, BB]{}.run(ballots, candidates, vacancies)()