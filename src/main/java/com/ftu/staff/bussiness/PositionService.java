package com.ftu.staff.bussiness;

import com.ftu.hibernate.HibernateUtil;
import com.ftu.staff.bo.Position;
import com.ftu.staff.dao.PositionDAO;
import org.hibernate.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PositionService implements Serializable {

    public List<Position> getAllWithParams(Position position, boolean search, int first, int pageSize, String sortField, Boolean desc) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.getAllWithParams(position, search, first, pageSize, sortField, desc);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public List<Position> getAll() {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.getAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public List<Position> findByPositonCodeId(String positionCode, Long positionId) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.findByPositonCodeId(positionCode,positionId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
    public void saveOrUpdate(Position obj) {
        try {
            PositionDAO dao = new PositionDAO();
            dao.saveOrUpdate(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public void delete(Position obj) {
        try {
            PositionDAO dao = new PositionDAO();
            dao.delete(obj);
            HibernateUtil.commitCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public Position findByPositionCode(String positionCode) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.findByPositionCode(positionCode);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }
    public Position findById(Long id) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.findById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public List<Position> findByShopId(Long shopId) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.findByShopId(shopId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public List<Position> findByShopIdAllChi(Long shopId) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.findByShopIdAllChi(shopId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public List<Position> findByShopIdActive(Long shopId) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.findByShopIdActive(shopId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public List<Position> getPositionNotSetting(Long shopId) {
        try {
            PositionDAO dao = new PositionDAO();
            return dao.getPositionNotSetting(shopId);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                HibernateUtil.closeCurrentSessions();
            } catch (Exception ex1) {
                Logger.getLogger(PositionService.class.getName()).log(Level.SEVERE, null, ex1);
                ex1.printStackTrace();
            }
        }
        return null;
    }

}
