package votee.algorithms

import org.scalatest.flatspec.AnyFlatSpec
import votee.UnitSpec
import votee.models.{ PreferentialBallot, PreferentialCandidate, Winner }
import votee.utils.Parser

/** Created by Abanda Ludovic on 06/05/2022. */

class ApprovalSpec extends AnyFlatSpec with UnitSpec:

  val expectedWinners = Seq(PreferentialCandidate("d", "D"))

  "Approval Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)

  def algorithmVerification(
      candidatesFile: String,
      ballotFile: String
  ): Seq[PreferentialCandidate] =
    val inputs = candidatesAndBallots(candidatesFile, ballotFile)
    Approval.run(inputs._2, inputs._1, 1).map(_.candidate)
