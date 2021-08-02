SELECT storage_type,Avg(worth_value) average_Value
FROM employee_item
         INNER JOIN items
                    ON employee_item.item_id = items.id
         INNER JOIN item_location
                    ON items.id = item_location.item_id
         LEFT JOIN locations
                   ON item_location.location_id = locations.id
WHERE  storage_type LIKE '%Room%'
GROUP  BY storage_type
ORDER  BY storage_type;