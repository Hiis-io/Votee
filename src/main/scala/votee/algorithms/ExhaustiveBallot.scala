package votee.algorithms

import com.typesafe.scalalogging.LazyLogging
import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}

import scala.annotation.tailrec

/**
 * Created by Abanda Ludovic on 01/04/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Exhaustive_ballot
 */

sealed trait ExhaustiveBallot[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    val candidateScoreMap = countFirstVotes(ballots, candidates)
    val sortedCandidateList = candidateScoreMap.toList.sortWith(_._2 < _._2)
    if candidateScoreMap.size > 2 then
      val losingCandidate =  sortedCandidateList.head
      val newBallots = for (ballot <- ballots) yield ballot -- List(losingCandidate._1)
      run(newBallots.asInstanceOf[List[B]], candidates.filter(_ != losingCandidate._1), vacancies)(tieResolver)
    else
      sortedCandidateList.map(Winner(_)).last::List()
  end run
end ExhaustiveBallot

case object  ExhaustiveBallot:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new ExhaustiveBallot[CC, BB]{}.run(ballots, candidates, vacancies)()