package com.antkorvin.damagetests.transactions;

import com.antkorvin.damagetests.services.RocketService;
import com.antkorvin.damagetests.utils.BaseSystemIT;
import com.github.database.rider.core.api.dataset.DataSet;
import org.assertj.core.api.Assertions;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by Korovin Anatolii on 30.11.17.
 * <p>
 * Тест readonly транзакций, показывает что:
 * <p>
 * - при использовании readonly транзакций, несколько операций чтения в одной транзакции выполняются через одно
 * подключение к БД
 * <p>
 * - если не пользоваться транзакциями, то каждое следующее чтение из БД будет получать новый connect к БД, что требует
 * ресурсов.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class ReadOnlyIT extends BaseSystemIT {

    @Autowired
    private RocketService rocketService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Возвращаем EntityManager который можно использовать даже вне транзакции
     */
    protected EntityManager getEntityManager() {
        return entityManager.getEntityManagerFactory().createEntityManager();
    }

    /**
     * Чистит статистику использования кэша hibernate
     */
    protected void clearHibernateCacheStatistics() {
        getEntityManager().unwrap(Session.class).getSessionFactory().getStatistics().clear();
    }

    /**
     * Получаем текущую статистику по БД
     */
    protected Statistics getStat() {
        return getEntityManager().unwrap(Session.class).getSessionFactory().getStatistics();
    }


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        clearHibernateCacheStatistics();
    }

    /**
     * тестируем чтение двух записей из readOnly транзакции
     * ожидаем что все операции пройдут в одном connect'е к БД
     */
    @Test
    @DataSet(cleanBefore = true, value = "datasets/rockets.json")
    public void testReadFromReadOnlyTransaction() {

        long connectCountBefore = getStat().getConnectCount();

        // Act
        rocketService.readWithReadOnlyTransaction(UUID.fromString("f26b93f8-a0ab-496c-8b24-3b446cb1c50c"),
                                                  UUID.fromString("d0bf699a-122a-44f4-91f6-3a1c2837167e"));

        long connectCountAfter = getStat().getConnectCount();

        // Asserts
        Assertions.assertThat(connectCountAfter - connectCountBefore).isEqualTo(1);
    }


    /**
     * не используя транзакции внутри вызываемого метода, ожидаем что для чтения будет открыто два соединения, заместо
     * использования одного
     */
    @Test
    @DataSet(cleanBefore = true, value = "datasets/rockets.json")
    public void testReadWithoutTransaction() {

        long connectCountBefore = getStat().getConnectCount();

        // Act
        rocketService.readWithoutTx(UUID.fromString("f26b93f8-a0ab-496c-8b24-3b446cb1c50c"),
                                    UUID.fromString("d0bf699a-122a-44f4-91f6-3a1c2837167e"));

        long connectCountAfter = getStat().getConnectCount();

        // Asserts
        Assertions.assertThat(connectCountAfter - connectCountBefore).isEqualTo(2);
    }


    @Test
    @DataSet(cleanBefore = true, value = "datasets/rockets.json")
    public void testReadFromNotReadOnlyTransaction() {

        long connectCountBefore = getStat().getConnectCount();

        // Act
        rocketService.readWithoutReadOnly(UUID.fromString("f26b93f8-a0ab-496c-8b24-3b446cb1c50c"),
                                          UUID.fromString("d0bf699a-122a-44f4-91f6-3a1c2837167e"));

        long connectCountAfter = getStat().getConnectCount();

        // Asserts
        Assertions.assertThat(connectCountAfter - connectCountBefore).isEqualTo(1);
    }

}
