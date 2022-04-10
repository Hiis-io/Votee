package votee.algorithms

import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}

import scala.annotation.tailrec

/**
 * Created by Abanda Ludovic on 01/04/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Exhaustive_ballot
 */

trait ExhaustiveBallotRule[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run[CC <: C, BB <: B](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[C]] = {
    val candidateScoreMap = countFirstVotes(ballots, candidates)
    val sortedCandidateList = candidateScoreMap.toList.sortWith(_._2 < _._2)
    if (candidateScoreMap.size > 2) {
      val losingCandidate =  sortedCandidateList.head
      val newBallots = for (ballot <- ballots) yield ballot.excludeCandidates(List(losingCandidate._1))
      run(ballots, candidates.filter(_ != losingCandidate._1), vacancies)
    } else {
      sortedCandidateList.map(Winner(_)).last::List()
    }
  }

final class ExhaustiveBallot[C <: Candidate] extends ExhaustiveBallotRule[C, Ballot[C]]