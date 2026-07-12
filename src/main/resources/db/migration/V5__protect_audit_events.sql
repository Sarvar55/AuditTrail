create or replace function reject_audit_event_mutation() returns trigger language plpgsql as $$
begin raise exception 'audit_events is append-only'; end; $$;
create trigger audit_events_append_only before update or delete on audit_events
for each statement execute function reject_audit_event_mutation();

-- Production hardening (execute as the table owner after migrations, replacing audit_app):
-- revoke update, delete, truncate on audit_events from audit_app;
-- grant select, insert on audit_events to audit_app;
