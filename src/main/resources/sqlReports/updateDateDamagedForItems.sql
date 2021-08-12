UPDATE
    items
SET
    date_damaged = '%s'
WHERE
        id IN
        (
            SELECT
                item_id
            FROM
                item_location
            WHERE
                    items.id = item_location.item_id
              AND item_location.location_id =
                  (
                      SELECT
                          locations.id
                      FROM
                          locations
                      WHERE
                              item_location.location_id = locations.id
                        AND storage_type LIKE '%%%s%%'
                        AND '%s' BETWEEN date_when_put AND CURRENT_DATE
--                           (
--                               SELECT
--                                   ifnull(date_when_put, CURRENT_DATE)
--                               FROM
--                                   locations
--                               WHERE
--                                       locations.id = item_location.location_id + 1
--                           )
                  )
        )
;