package votee.models

import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait Election[C <: Candidate, B <: Ballot[C], W <: Winner[C]]:
  /**
   * A naive Tie Resolver that takes the first N leading candidates.
   * This doesn't take into consideration that some of the other candidates left out may have the same scores as the leading ones.
   */
  protected val DEFAULT_TIE_RESOLVER: TieResolver[C] = (candidateScores: List[(C, Rational)], vacancies: Int) => candidateScores.sortWith(_._2 > _._2).take(vacancies)
  
  def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[W]
end Election


trait PreferentialElection[C <: Candidate, B <: Ballot[C]] extends Election[C, B, Winner[C]]:
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