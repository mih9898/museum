select name, locations.storage_type, round((CURRENT_DATE - locations.date_when_put)) Days
from items
         inner join (select item_id, min(location_id) location_id
                     from item_location
                     group by item_id) item_location
                    on items.id=item_location.item_id
         left join locations
                   on item_location.location_id=locations.id
where items.is_lost=0
union
select name, locations.storage_type, round((lost_items.date_lost - locations.date_when_put)) Days
from items
         inner join lost_items
                    on items.id=lost_items.item_id
         inner join (select item_id, min(location_id) location_id
                     from item_location
                     group by item_id) item_location
                    on items.id=item_location.item_id
         left join locations
                   on item_location.location_id=locations.id
where items.is_lost=1;