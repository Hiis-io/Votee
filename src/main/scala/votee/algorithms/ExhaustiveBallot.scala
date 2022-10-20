package votee.algorithms

import scala.annotation.tailrec
import votee.models._

/**
 * Created by Abanda Ludovic on 01/04/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Exhaustive_ballot
 */

sealed trait ExhaustiveBallot[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): Seq[Winner[C]] =
    val candidateScoreMap = countFirstVotes(ballots, candidates)
    val sortedCandidateList = candidateScoreMap.toList.sortWith(_._2 < _._2)
    if candidateScoreMap.size > 2 then
      val losingCandidate =  sortedCandidateList.head
      val newBallots: Seq[B[C]] = for (ballot <- ballots) yield ballot -- Seq(losingCandidate._1)
      run(newBallots, candidates.filter(_ != losingCandidate._1), vacancies)
    else
      Seq(sortedCandidateList.map(Winner(_)).last)
  end run
end ExhaustiveBallot

case object  ExhaustiveBallot:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]): Seq[Winner[C]] = new ExhaustiveBallot[C, B]{}.run(ballots, candidates, vacancies)
end ExhaustiveBallot
