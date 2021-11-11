package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InvoiceItem extends Model {

    Long invoiceLineId;
    Long invoiceId;
    Long trackId;
    BigDecimal unitPrice;
    Long quantity;

    public Track getTrack() {
        return null;
    }
    public Invoice getInvoice() {
        return null;
    }

    public Long getInvoiceLineId() {
        return invoiceLineId;
    }

    public void setInvoiceLineId(Long invoiceLineId) {
        this.invoiceLineId = invoiceLineId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean verify() {
        _errors.clear(); // clear any existing errors
        if (unitPrice == null || "".equals(unitPrice)) {
            addError("Unit price can't be null or blank!");
        }
        if (quantity == null || "".equals(quantity)) {
            addError("Quantity can't be null or blank!");
        }
        return !hasErrors();
    }

    @Override
    public boolean update() {
        if (verify()) {
            try (Connection conn = DB.connect();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE invoiceItems SET InvoiceId=?, TrackId=?, UnitPrice=?, Quantity=? WHERE InvoiceLineId=?")) {
                stmt.setLong(1, this.getInvoiceId());
                stmt.setLong(2, this.getTrackId());
                stmt.setBigDecimal(3, this.getUnitPrice());
                stmt.setLong(4, this.getQuantity());
                stmt.setLong(5, this.getInvoiceLineId());
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
                         "INSERT INTO invoiceItems (InoviceId, TrackId, UnitPrice, Quantity) VALUES (?, ?, ?, ?)")) {
                stmt.setLong(1, this.getInvoiceId());
                stmt.setLong(2, this.getTrackId());
                stmt.setBigDecimal(3, this.getUnitPrice());
                stmt.setLong(4, this.getQuantity());
                stmt.executeUpdate();
                invoiceLineId = DB.getLastID(conn);
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
                     "DELETE FROM invoiceItems WHERE InvoiceLineID=?")) {
            stmt.setLong(1, this.getInvoiceLineId());
            stmt.executeUpdate();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
