-- Emmits a notification to the c channel
--Procedure
CREATE OR REPLACE FUNCTION notifySnapshotSave()
  RETURNS trigger AS $$
DECLARE
  channel varchar;
BEGIN
  channel := 'c';
  IF TG_OP = 'DELETE' THEN
    PERFORM pg_notify(channel, OLD.id::text);
    RETURN OLD;
  ELSE
    PERFORM pg_notify(channel, NEW.id::text);
    RETURN NEW;
  END IF;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER onNotifySnapshotSave
  AFTER INSERT OR UPDATE OR DELETE
  ON VerifierAttributeSnapshot
  FOR EACH ROW
  EXECUTE PROCEDURE notifySnapshotSave();
-- End of Emmits a notification to the vau-officeId channel. vau = Verifier Attribute Update

-- Updates the lowestReading and highestReading properties in the VerifierAttributeSnapshot table
CREATE OR REPLACE FUNCTION updateSnapshotEdges()
/**
 * Sets automatically the {@link VerifierAttributeSnapshot#getLowestReading()} and the
 * {@link VerifierAttributeSnapshot#getHighestReading()} with the corresponding value.
 * 
 * if (lastReading < lowestReading)
 *   lowestReading = lastReading
 *   
 * if (lastReading > highestReading)
 *   highestReading = lastReading
 */
  RETURNS trigger AS $$
BEGIN
  IF NEW.lastReading < NEW.lowestReading OR NEW.lowestReading IS NULL THEN
    NEW.lowestReading := NEW.lastReading;
  END IF;

  IF NEW.lastReading > NEW.highestReading OR NEW.highestReading IS NULL THEN
    NEW.highestReading := NEW.lastReading;
  END IF;

  RETURN NEW;
END;
$$ LANGUAGE 'plpgsql' VOLATILE;

CREATE TRIGGER onUpdateSnapshotEdges
  BEFORE INSERT OR UPDATE
  ON VerifierAttributeSnapshot
  FOR EACH ROW
  EXECUTE PROCEDURE updateSnapshotEdges();
-- End of Updates the lowestReading and highestReading properties in the VerifierAttributeSnapshot table