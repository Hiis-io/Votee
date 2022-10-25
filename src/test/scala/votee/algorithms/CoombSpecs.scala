package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import votee.UnitSpec
import votee.models.PreferentialCandidate
import votee.utils.Parser

/** Created by Abanda Ludovic on 09/05/2022. */

class CoombSpecs extends AnyFlatSpec with UnitSpec:

  val expectedWinners = List(PreferentialCandidate("a", "A"))

  "Coomb's Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)

  def algorithmVerification(
      candidatesFile: String,
      ballotFile: String
  ): Seq[PreferentialCandidate] =
    val inputs = candidatesAndBallots(candidatesFile, ballotFile)
    Coomb.run(inputs._2, inputs._1, 1).map(_.candidate)
