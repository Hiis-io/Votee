package votee.models

import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait Election[C <: Candidate, B <: Ballot[C], W <: Winner[C]]:
  def run(ballots: List[B], candidates: List[C], vacancies: Int): List[W]


trait PreferentialElection[C <: Candidate, B <: PreferenceBallot[C]] extends Election[C, B, Winner[C]]:
  val MAJORITY_THRESHOLD: Rational = Rational(1,2)

  def countFirstVotes(ballots: List[B], candidates: List[C]): Map[C, Rational] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]

    //We are interested only in the first valid candidate in the ballot
    ballots.foreach { ballot =>
      ballot.preferences.find(candidates.contains(_)) match {
        case Some(candidate) => candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
        case _ =>
      }
    }
    Map.empty ++ candidateScoreMap
  end countFirstVotes

  def countLastVotes(ballots: List[B], candidates: List[C]): Map[C, Rational] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]

    //We are interested only in the last valid candidate in the ballot
    ballots.foreach { ballot =>
      ballot.preferences.findLast(candidates.contains(_)) match {
        case Some(candidate) => candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
        case _ =>
      }
    }
    Map.empty ++ candidateScoreMap
  end countLastVotes
  
end PreferentialElection