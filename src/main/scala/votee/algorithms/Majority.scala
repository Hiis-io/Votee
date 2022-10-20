package votee.algorithms

import spire.math.Rational

import votee.models._

/**
 * Created by Abanda Ludovic on 29/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Majority_rule
 */

sealed trait Majority[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]] extends PreferentialElection[C, B]:
  
  override final def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): Seq[Winner[C]] =
    countFirstVotes(ballots, candidates)
      .toList.sortWith(_._2 > _._2)
      .map(Winner(_))
      .filter(w => w.score > Rational(ballots.length) * MAJORITY_THRESHOLD)
      .take(vacancies)
  end run
end Majority

case object  Majority:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]): Seq[Winner[C]] = new Majority[C, B]{}.run(ballots, candidates, vacancies)
end Majority