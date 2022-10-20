package votee.algorithms

import spire.math.Rational
import votee.models.*

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm as described at https://en.wikipedia.org/wiki/Borda_count
 */


sealed trait BordaCount[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]] extends PreferentialElection[C, B]:
  override final def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): Seq[Winner[C]] =
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
    resolveTies(candidateScoreMap.toList.sortWith(_._2 > _._2)).take(vacancies).map(Winner(_))
  end run
end BordaCount

case object  BordaCount:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]): Seq[Winner[C]] = new BordaCount[C, B]{}.run(ballots, candidates, vacancies)
end BordaCount
