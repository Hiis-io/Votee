package votee.algorithms

import spire.math.Rational
import votee.models.*

import scala.annotation.tailrec
import scala.collection.mutable
/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm as described at https://en.wikipedia.org/wiki/Nanson%27s_method#Baldwin_method
 */

sealed trait BaldWin[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): Seq[Winner[C]] =
    if candidates.length == 1 then
      bordaScores(ballots, candidates).toList.map(Winner(_))
    else
      // removing the lowest borda score candidate from the candidate list
      val lowestBordaCandidate = resolveTies(bordaScores(ballots, candidates).toList.sortWith(_._2 < _._2)).head
      run(ballots, candidates.filter(_ != lowestBordaCandidate._1), vacancies)
  end run

  private def bordaScores(ballots: Seq[B[C]], candidates: Seq[C]): mutable.HashMap[C, Rational] =
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

case object  BaldWin:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]): Seq[Winner[C]] = new BaldWin[C, B]{}.run(ballots, candidates, vacancies)
end BaldWin
