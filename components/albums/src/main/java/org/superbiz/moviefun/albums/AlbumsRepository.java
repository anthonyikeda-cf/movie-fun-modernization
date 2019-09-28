package org.superbiz.moviefun.albums; /**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.List;

@Repository
public class AlbumsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addAlbum(Album album) {
        entityManager.persist(album);
    }

    public Album find(long id) {
        return entityManager.find(Album.class, id);
    }

    public List<Album> getAlbums() {
        CriteriaQuery<Album> cq = entityManager.getCriteriaBuilder().createQuery(Album.class);
        cq.select(cq.from(Album.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<Album> findAll(int firstResult, int maxResults) {
        CriteriaQuery<Album> cq = entityManager.getCriteriaBuilder().createQuery(Album.class);
        cq.select(cq.from(Album.class));
        TypedQuery<Album> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    @Transactional
    public void deleteAlbum(Album album) {
        entityManager.remove(album);
    }

    @Transactional
    public void deleteAlbumId(Long id) {
        Album album = entityManager.find(Album.class, id);
        deleteAlbum(album);
    }

    @Transactional
    public void updateAlbum(Album album) {
        entityManager.merge(album);
    }

    public int countAll() {
        CriteriaQuery<Long> cq = entityManager.getCriteriaBuilder().createQuery(Long.class);
        Root<Album> rt = cq.from(Album.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = entityManager.createQuery(cq);
        return (q.getSingleResult()).intValue();
    }

    public int count(String field, String searchTerm) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<Album> root = cq.from(Album.class);
        EntityType<Album> type = entityManager.getMetamodel().entity(Album.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.select(qb.count(root));
        cq.where(condition);

        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    public List<Album> findRange(String field, String searchTerm, int firstResult, int maxResults) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Album> cq = qb.createQuery(Album.class);
        Root<Album> root = cq.from(Album.class);
        EntityType<Album> type = entityManager.getMetamodel().entity(Album.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.where(condition);
        TypedQuery<Album> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

}
