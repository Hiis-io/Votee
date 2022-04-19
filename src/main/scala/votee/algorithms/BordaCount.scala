package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm as described at https://en.wikipedia.org/wiki/Borda_count
 */

sealed trait BordaCount[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
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

case object  BordaCount:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new BordaCount[CC, BB]{}.run(ballots, candidates, vacancies)()