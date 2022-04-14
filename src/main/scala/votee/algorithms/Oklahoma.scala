package votee.algorithms

import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 01/04/2022.https://en.wikipedia.org/wiki/Oklahoma_primary_electoral_system
 * we are not enforcing a strict number of preferences per ballot, unlike in the original Oklahoma method
 * so ballots with fewer preferences are not voided
 */
 //TODO Fix Algorithm
sealed trait Oklahoma[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] = ???

  def oklahomaTotals(ballots: List[B], candidates: List[C], candidateScoresMap: mutable.HashMap[C, Rational], multiplier: Rational):List[(Candidate, Rational)] = {
    val scoresMap = candidateScoresMap
    val candidateTotalScores = countFirstVotes(ballots, candidates)
    for (candidate <- candidates) {
      scoresMap(candidate) = scoresMap.getOrElse(candidate, Rational(0, 1)) + candidateTotalScores.getOrElse(candidate, Rational(0, 1)) * multiplier
    }
    val sortedCandidateScoreMap = scoresMap.toList.sortWith(_._2 > _._2)
    if (sortedCandidateScoreMap.head._2 > MAJORITY_THRESHOLD * Rational(ballots.length)) {
      sortedCandidateScoreMap.head :: List.empty
    } else {
      val newBallots: List[B] = List.empty
      ballots.foreach(_.excludeCandidates(candidates):: newBallots)
      oklahomaTotals(newBallots, candidates, scoresMap, Rational(multiplier.numerator, multiplier.denominator + 1))
    }
  }
