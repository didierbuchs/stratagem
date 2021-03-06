/*
Stratagem is a model checker for transition systems described using rewriting
rules and strategies.
Copyright (C) 2013 - SMV@Geneva University.
Program written by Edmundo Lopez Bobeda <edmundo [at] lopezbobeda.net>.
This program is free software; you can redistribute it and/or modify
it under the  terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ch.unige.cui.smv.stratagem.transformers

import java.io.File
import com.typesafe.scalalogging.slf4j.Logging
import ch.unige.cui.smv.stratagem.petrinets.PetriNet
import ch.unige.cui.smv.stratagem.petrinets.Place
import java.io.FileNotFoundException
import ch.unige.cui.smv.stratagem.petrinets.Transition
import ch.unige.cui.smv.stratagem.petrinets.Arc

/**
 * This object takes a file and transforms it to a stratagem transition system.
 *
 * @author mundacho
 *
 */
object PNML2PetriNet extends Logging with ((File*) => PetriNet) {

  var name: String = ""

  /**
   * Transforms a pnml file into a representation of a Petri Net.
   * @param a file pnml P/T format.
   * @return a petri net.
   */
  def apply(input: File*) = {
    require(input.size == 1)
    val xml = loadFile(input.head)
    val ptType = (xml \ "net" \ "@type").text
    if (ptType != "http://www.pnml.org/version-2009/grammar/ptnet") {
      logger.error("Petri net type not supported. Only Place / Transitions nets are supported.")
      System.exit(-1)
    }
    // first we create an adt
    val nameList = xml \ "net" \ "name" \ "text"
    name = extractName(nameList)
    logger.debug(s"Name of the petri net is $name")
    val places = Map((xml \ "net" \ "page" \ "place").map(p => (p \ "@id").text -> new Place(
      (p \ "@id").text, // extract id
      (p \ "name" \ "text").text, // extract name
      if ((p \ "initialMarking" \ "text").size == 0) 0 else (p \ "initialMarking" \ "text").text.toInt) // extract initial marking
      ).toArray: _*)
    val arcs = xml \ "net" \ "page" \ "arc"
    val arcsBySource = arcs.groupBy(arc => (arc \ "@source").text)
    val arcsByTarget = arcs.groupBy(arc => (arc \ "@target").text)
    logger.debug(s"Number of places is ${places.values.toSet.size}")
    val transitions = (xml \ "net" \ "page" \ "transition").map(t =>
      new Transition(
        (t \ "@id").text, // extract id
        (t \ "name" \ "text").text.replaceAll("[^a-zA-Z_0-9]", "_"), // extract name
        // now extract the input arcs
        arcsByTarget.getOrElse((t \ "@id").text, Set.empty).map(arc =>
          new Arc((arc \ "@id").text, // extract id of the arc
            places.getOrElse((arc \ "@source").text, places((arc \ "@target").text)), // extract the place for the arc
            if ((arc \ "inscription" \ "text").text.isEmpty) 1 else (arc \ "inscription" \ "text").text.toInt)).toSet,
        arcsBySource.getOrElse((t \ "@id").text, Set.empty).map(arc =>
          new Arc((arc \ "@id").text, // extract id of the arc
            places.getOrElse((arc \ "@source").text, places((arc \ "@target").text)), // extract the place for the arc
            if ((arc \ "inscription" \ "text").text.isEmpty) 1 else (arc \ "inscription" \ "text").text.toInt)).toSet))
    logger.debug(s"Number of transitions is ${transitions.size}")
    // now we create the petri net
    new PetriNet(name, places.values.toSet, transitions.toSet)
  }

  private def extractName(nameList: scala.xml.NodeSeq): String =
    if (nameList.isEmpty) {
      logger.warn("This petri net has no name")
      "anonymous petri net"
    } else if (nameList.size == 1) {
      nameList.head.text.replaceAll("[^a-zA-Z_0-9]", "_")
    } else {
      logger.warn("This file has several name tags, chosing the first one")
      nameList(1).text.replaceAll("[^a-zA-Z_0-9]", "_")
    }

  private def loadFile(input: java.io.File): scala.xml.Elem = {
    val res = try {
      Some(scala.xml.XML.loadFile(input))
    } catch {
      case ex: FileNotFoundException => {
        logger.error("Unable to open file " + input.getName())
        None
      }
    }
    if (res.isEmpty) System.exit(-1)
    res.get
  }
}