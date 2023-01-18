package io.hiis.votee.models

import spire.math.Rational

import scala.annotation.targetName
import scala.util.{ Failure, Success, Try }

/** Created by Abanda Ludovic on 29/03/2022. */

/**
 * Ballot represents the base contract of an election ballot implementation. User could define their
 * own ballot base on this contract to suite their needs.
 *
 * @param id
 *   the ballot id
 * @param weight
 *   the ballot weight
 * @param preferences
 *   the ballot preferences
 *
 * @tparam C
 *   The ballot candidate type
 * @tparam T
 *   The ballot concrete type. Required for some utility methods
 * @see
 *   [[PreferentialBallot]]
 */
trait Ballot[+C <: Candidate, +T[+CC >: C <: Candidate] <: Ballot[CC, T]](
    val id: Int,
    val weight: Rational,
    val preferences: Seq[C]
) extends Product
    with Serializable:

  /**
   * Excludes the desired candidates found in the list from the ballot
   *
   * @param candidates
   *   the candidates to excludes
   * @tparam CC
   *   candidates type
   * @return
   *   new ballot with some excluded candidates
   */
  @targetName("exclude")
  def --[CC >: C <: Candidate](candidates: Seq[CC]): T[CC]

  /**
   * Includes the desired candidates found in the list from the ballot Note: these candidates are
   * assumed by the algorithms to be appended to the candidates list.
   *
   * @param candidates
   *   the candidates too be included
   * @tparam CC
   *   candidates type
   * @return
   *   new ballot with some included candidates
   */

  @targetName("include")
  def ++[CC >: C <: Candidate](candidates: Seq[CC]): T[CC]
end Ballot

/**
 * An implementation of the ballot contract. Should generally suite the users needs
 *
 * @param id
 *   the ballot id
 * @param weight
 *   the ballot weight
 * @param preferences
 *   the ballot preferences
 * @tparam C
 *   The ballot candidate type
 */
final case class PreferentialBallot[+C <: Candidate](
    override val id: Int,
    override val weight: Rational = Rational(1, 1),
    override val preferences: Seq[C]
) extends Ballot[C, PreferentialBallot](id, weight, preferences):

  /**
   * Excludes the desired candidates found in the list from the ballot
   *
   * @param candidates
   *   the candidates to excludes
   * @tparam CC
   *   candidates type
   * @return
   *   new ballot with some excluded candidates
   */
  @targetName("exclude")
  override def --[CC >: C <: Candidate](candidates: Seq[CC]): PreferentialBallot[CC] =
    PreferentialBallot(id, weight, preferences.filterNot(candidates.contains(_)))

  /**
   * Includes the desired candidates found in the list from the ballot Note: these candidates are
   * assumed by the algorithms to be appended to the candidates list.
   *
   * @param candidates
   *   the candidates too be included
   * @tparam CC
   *   candidates type
   * @return
   *   new ballot with some included candidates
   */
  @targetName("include")
  override def ++[CC >: C <: Candidate](candidates: Seq[CC]): PreferentialBallot[CC] =
    PreferentialBallot(id, weight, candidates ++ preferences)

end PreferentialBallot
