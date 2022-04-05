package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm as described at https://en.wikipedia.org/wiki/Nanson%27s_method#Baldwin_method
 */

trait BaldWin[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
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
        ballot.preferences.filter(candidate => candidates.contains(candidate)).zipWithIndex.foreach(candidateWithIndex => {
          candidateScoreMap(candidateWithIndex._1) =
            candidateScoreMap.getOrElse(candidateWithIndex._1, Rational(0, 1)) +
              (Rational(candidates.length - 1 - candidateWithIndex._2) * ballot.weight)
        })
    }
    candidateScoreMap

  end bordaScores
end BaldWin


case object BaldWin extends BaldWin[PreferentialCandidate, PreferentialBallot[PreferentialCandidate]]