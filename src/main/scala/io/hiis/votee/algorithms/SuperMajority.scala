package io.hiis.votee.algorithms

import io.hiis.votee.models.{
  Ballot,
  Candidate,
  Election,
  PreferentialElection,
  TieResolver,
  Winner
}
import spire.math.Rational

/**
 * Created by Abanda Ludovic on 30/03/2022. Algorithm described at
 * https://en.wikipedia.org/wiki/Supermajority
 */

sealed trait SuperMajority[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](
    majorityPercentage: Rational = Rational(1, 2)
) extends PreferentialElection[C, B]:
  require(!(majorityPercentage < Rational(1, 2)) && !(majorityPercentage > Rational(1)))
  final override def run(ballots: Seq[B[C]], candidates: Seq[C], vacancies: Int)(using
      tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER
  ): Seq[Winner[C]] =
    countFirstVotes(ballots, candidates).toSeq
      .sortWith(_._2 > _._2)
      .map(Winner(_))
      .filter(w => w.score > Rational(ballots.length) * majorityPercentage)
      .take(vacancies)
  end run
end SuperMajority

case object SuperMajority:
  def run[C <: Candidate, B[+CC >: C <: Candidate] <: Ballot[CC, B]](
      ballots: Seq[B[C]],
      candidates: Seq[C],
      vacancies: Int,
      majorityPercentage: Rational = Rational(1, 2)
  )(using
      tieResolver: TieResolver[C] = Election.TieResolvers.doNothingTieResolver[C]
  ): Seq[Winner[C]] =
    new SuperMajority[C, B](majorityPercentage) {}.run(ballots, candidates, vacancies)
end SuperMajority
