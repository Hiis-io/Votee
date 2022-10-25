package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import spire.math.Rational
import votee.UnitSpec
import votee.models.PreferentialCandidate
import votee.utils.Parser

/** Created by Abanda Ludovic on 06/05/2022. */

class SuperMajoritySpec extends AnyFlatSpec with UnitSpec:

  val expectedWinners = List.empty

  "Super-Majority Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)

  def algorithmVerification(
      candidatesFile: String,
      ballotFile: String
  ): Seq[PreferentialCandidate] =
    val inputs = candidatesAndBallots(candidatesFile, ballotFile)
    SuperMajority.run(inputs._2, inputs._1, 1, Rational(6, 10)).map(_.candidate)
