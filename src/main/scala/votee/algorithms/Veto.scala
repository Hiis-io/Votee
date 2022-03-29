package votee.algorithms

import votee.models.{Candidate, Election, PreferenceBallot, PreferentialCandidate, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

case object Veto extends Election[PreferentialCandidate, PreferenceBallot[PreferentialCandidate], Winner[PreferentialCandidate]] {
  override def run(ballots: List[PreferenceBallot[PreferentialCandidate]], candidates: List[PreferentialCandidate], vacancies: Int): List[Winner[PreferentialCandidate]] = {
    val candidateScoreMap = new mutable.HashMap[PreferentialCandidate, Rational]
    for (c <- candidates) {
      candidateScoreMap(c) = Rational(0, 1)
    }

    for (ballot <- ballots) {
      for (preference <- ballot.preferences) {
        if (!(preference == ballot.preferences.last && ballot.preferences.length > 1)) {
          //Automatically assign a weight of 1 to all candidates in the ballot except the last candidate as described by the veto rule
          candidateScoreMap(preference) = candidateScoreMap.getOrElse(preference, Rational(0, 1)) + Rational(1, 1)
        }
      }
    }
    candidateScoreMap.toList.sortWith(_._2 > _._2).take(vacancies).map(Winner(_))
  }

}
