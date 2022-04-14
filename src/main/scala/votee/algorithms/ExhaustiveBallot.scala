package votee.algorithms

import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}

import scala.annotation.tailrec

/**
 * Created by Abanda Ludovic on 01/04/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Exhaustive_ballot
 */

sealed trait ExhaustiveBallot[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = countFirstVotes(ballots, candidates)
    val sortedCandidateList = candidateScoreMap.toList.sortWith(_._2 < _._2)
    if candidateScoreMap.size > 2 then
      val losingCandidate =  sortedCandidateList.head
      val newBallots: List[B] = List.empty
      ballots.foreach(_.excludeCandidates(List(losingCandidate._1)) :: newBallots)
      run(newBallots, candidates.filter(_ != losingCandidate._1), vacancies)
    else
      sortedCandidateList.map(Winner(_)).last::List()
  end run
end ExhaustiveBallot

case object  ExhaustiveBallot:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new ExhaustiveBallot[CC, BB]{}.run(ballots, candidates, vacancies)