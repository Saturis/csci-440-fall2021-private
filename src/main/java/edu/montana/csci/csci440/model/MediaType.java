package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;
import org.eclipse.jetty.http.MetaData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MediaType extends Model {

    private Long mediaTypeId;
    private String name;

    private MediaType(ResultSet results) throws SQLException {
        name = results.getString("Name");
        mediaTypeId = results.getLong("MediaTypeId");
    }

    public Long getMediaTypeId() {
        return mediaTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<MediaType> all() {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM media_types"
             )) {
            ResultSet results = stmt.executeQuery();
            List<MediaType> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new MediaType(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static List<MediaType> all(int page, int count) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM artists LIMIT ? OFFSET ?"
             )) {
            stmt.setInt(1, count);
            stmt.setInt(2, (page - 1) * count);
            ResultSet results = stmt.executeQuery();
            List<MediaType> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new MediaType(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public boolean verify() {
        _errors.clear(); // clear any existing errors
        if (name == null || "".equals(name)) {
            addError("Name can't be null or blank!");
        }
        return !hasErrors();
    }

    @Override
    public boolean update() {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE mediaTypes SET Name=? WHERE MediaTypeId=?")) {
                stmt.setString(1, this.getName());
                stmt.setLong(2, this.getMediaTypeId());
                stmt.executeUpdate();
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean create() {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO mediaTypes (Name) VALUES (?)")) {
                stmt.setString(1, this.getName());
                stmt.executeUpdate();
                mediaTypeId = DB.getLastID(conn);
                return true;
            } catch (SQLException sqlException) {
                throw new RuntimeException(sqlException);
            }
        } else {
            return false;
        }
    }

    @Override
    public void delete() {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM mediaTypes WHERE MediaTypesId=?")) {
            stmt.setLong(1, this.getMediaTypeId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}