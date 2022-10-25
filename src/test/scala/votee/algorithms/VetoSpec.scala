package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import votee.UnitSpec
import votee.models.PreferentialCandidate
import votee.utils.Parser

/** Created by Abanda Ludovic on 06/05/2022. */

class VetoSpec extends AnyFlatSpec with UnitSpec:

  val expectedWinners = List(PreferentialCandidate("d", "D"))

  "Veto Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)

  def algorithmVerification(
      candidatesFile: String,
      ballotFile: String
  ): Seq[PreferentialCandidate] =
    val inputs = candidatesAndBallots(candidatesFile, ballotFile)
    Veto.run(inputs._2, inputs._1, 1).map(_.candidate)
