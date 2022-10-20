package votee.algorithms

import spire.math.Rational
import votee.models._

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Approval_voting
 */

sealed trait Approval[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]] extends PreferentialElection[C, B]:
  override final def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): Seq[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    for (ballot <- ballots)
      for(candidate <- ballot.preferences)
        candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
    resolveTies(candidateScoreMap.toSeq.sortWith( _._2 > _._2 )).take(vacancies).map(Winner(_))
  end run
end Approval

object Approval:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]): Seq[Winner[C]] = new Approval[C, B]{}.run(ballots, candidates, vacancies)
end Approval