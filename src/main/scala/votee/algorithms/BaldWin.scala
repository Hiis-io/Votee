package votee.algorithms

import votee.models.{Candidate, Election, PreferenceBallot, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm as described at https://en.wikipedia.org/wiki/Borda_count
 */

trait BaldWin [C <: Candidate, B <: PreferenceBallot[C]] extends Election[C, B, Winner[C]]:
  override def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =

    if (candidates.length == 1)
      bordaScores(ballots, candidates).toList.map(Winner(_))
    else
      // removing the lowest borda score candidate from the candidate list
      val lowestBordaCandidate = bordaScores(ballots, candidates).toList.sortWith(_._2 < _._2).head
      run(ballots, candidates.filter(_ != lowestBordaCandidate._1), vacancies)

  private def bordaScores(ballots: List[B], candidates: List[C]): mutable.HashMap[C, Rational] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]

    //Calculate Border Scores for the candidates
    ballots.foreach { ballot =>
      if ballot.preferences.nonEmpty then
        // need to take the size of the list first and then calculate the borda scores
        var bordaCounter = candidates.length
        ballot.preferences.filter(candidate => candidates.contains(candidate)).map(candidate => {
          candidateScoreMap(candidate) = candidateScoreMap.getOrElse(candidate, Rational(0, 1)) + (Rational(bordaCounter - 1) * ballot.weight)
          bordaCounter -= 1
        })
    }
    candidateScoreMap
