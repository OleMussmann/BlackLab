# BlackLab Server API redesign

The BLS API has quite a few quirks that can make it confusing and annoying to work with.
If we break compatibility anyway (e.g. because we're integrating with Solr), how might
we redesign the API?


## Maintain support for old API?

Consider supporting the old API for a while longer.

We don't want a lot of duplicated maintenance, but maybe an adapter could
be created that translates between the old and new API.

The experimental aggregator could be used as a starting point, because it already
implements most of the JAXB-annotated classes required.


## Goals for new API

General:
- Document the changes<br>(make sure there's a clear migration guide available)
- Don't stray too far<br>(don't change things for change's sake)
- Try to make the transition smooth.<br>
  (when possible, consider keeping the response the same and
  change the parameter and/or configuration setting while still supporting the old ones)
- Ensure correct data types.<br>
  (e.g. `fieldValues` should have integer values, but are strings.)
- Eliminate inconsistencies in response structure.<br>
  (if information is given in multiple places, e.g. on the server info page as well
   as on the index info page, use the same structure and element names (except one page
   may give additional details))
- Change confusing names.<br>
  (e.g. the name `stoppedRetrievingHits` prompts the question "why did you stop?".
  `reachedHitLimit` might be easier to understand, especially if it's directly 
  related to a configuration setting `hitLimit`)
- Try to use consistent terminology between parameters, response and configuration files.<br>
  (e.g. use the term "hitLimit" everywhere for the same concept)
- Group related values.<br>
  (e.g. numberOfHitsRetrieved / numberOfDocsRetrieved / stoppedRetrievingHits
  would be better as a structure `"retrieved": { "hits": 100, "docs": "10", "reachedHitLimit": true }` ).
- Handle custom information better. <br>
  Custom information, ignored by BlackLab but useful for e.g. the frontend,
  like displayName, uiType, etc. is polluting the response structure.
  We should isolate it (e.g. in a `custom` section for each field, annotation, etc.),
  just pass it along unchecked, and include it only if requested.
- Don't include static info on dynamic (results) pages.<br>
  (e.g. don't send display names for all metadata fields with each hits results;
   the client can request those once if needed)
- Replace `left`/`right` with `before`/`after`<br>
  (makes more sense for RTL languages)


JSON-related:
- Consider avoiding dynamic keys<br>
(e.g. having annotatedFields be an object with the field name
  as the key). There's no problems with encoding this (like in XML), but it does mean you can't 
  easily specify a schema for the JSON structure. Also it makes (de)serialization messier (need 
  custom (de)serializers, etc.).<br>
  On the other hand, it can sometimes be convenient for the client to be able to 
  look up fields by name. Iterating over fields on the other hand is a little less convient 
  with objects than with arrays, so it might be a wash.


XML-related:
- JSON should probably be our primary output format<br>
  (the XML structure should just be a dumb translation from JSON, for those who need it, 
  e.g. to pass through XSLT). So e.g. no difference in concordance structure between JSON and XML)
- Avoid attributes; use elements for everything.
- Avoid dynamic XML element names<br>(e.g. don't use map keys for XML element names.
  Not an issue if we copy JSON structure)
- When using `usecontent=orig`, don't make the content part of the XML anymore.<br>
  (escape it using CDATA (again, same as in JSON). Also consider just returning both
  the FI concordances as well as the original content (if requested), so the response
  structure doesn't fundamentally change because of one parameter value)
  (optionally have a parameter to include it as part of the XML if desired, to simplify response handling?)