package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferenceBallot, PreferentialCandidate, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm as described at https://en.wikipedia.org/wiki/Borda_count
 */

trait BordaCount[C <: Candidate, B <: Ballot[C]] extends Election[C, B, Winner[C]]:
  override def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =
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
    candidateScoreMap.toList.sortWith(_._2 > _._2).take(vacancies).map(Winner(_))

  end run
end BordaCount


case object BordaCount extends BordaCount[PreferentialCandidate, PreferenceBallot[PreferentialCandidate]]
