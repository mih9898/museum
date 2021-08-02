select name, locations.storage_type, locations.description
from items
         inner join (select item_id, max(location_id) location_id
                     from item_location
                     group by item_id) item_location
                    on items.id=item_location.item_id
         left join locations
                   on item_location.location_id=locations.id
where items.is_lost=0;