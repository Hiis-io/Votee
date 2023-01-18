package io.hiis.votee.algorithms

import io.hiis.votee.UnitSpec
import io.hiis.votee.algorithms.BaldWin
import io.hiis.votee.models.{ PreferentialBallot, PreferentialCandidate }
import io.hiis.votee.utils.Parser
import org.scalatest.flatspec.AnyFlatSpec

/** Created by Abanda Ludovic on 09/05/2022. */

class BaldWinSpec extends AnyFlatSpec with UnitSpec:

  val expectedWinners = List(PreferentialCandidate("a", "A"))

  "Approval Algorithm Test" should
    "verify result" in
    assert(algorithmVerification("01-candidates.json", "03-ballots.json") === expectedWinners)

  def algorithmVerification(
      candidatesFile: String,
      ballotFile: String
  ): Seq[PreferentialCandidate] =
    val inputs = candidatesAndBallots(candidatesFile, ballotFile)
    BaldWin.run(inputs._2, inputs._1, 1).map(_.candidate)
