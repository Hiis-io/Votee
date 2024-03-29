package io.hiis.votee.models

import spire.math.Rational

/** Created by Abanda Ludovic on 19/04/2022. */

/**
 * TieResolver describes the contract on how to resolve candidates with the same scores. Given a
 * list of candidates with the same score return a new list with a desired ordering.
 *
 * The resolve function must satisfy the following conditions:
 *
 *   1. Total number of elements in the original list should be same as that of returned list 2. All
 *      elements in the original list should be found in the returned list
 * @tparam C
 * @see
 *   [[Election.TieResolvers]]
 */
trait TieResolver[C <: Candidate]:
  def resolve(candidateScores: Seq[(C, Rational)]): Seq[(C, Rational)]
