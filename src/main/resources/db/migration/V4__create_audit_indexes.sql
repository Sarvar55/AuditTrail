create index ix_audit_events_actor_id on audit_events(actor_id);
create index ix_audit_events_action_type on audit_events(action_type);
create index ix_audit_events_created_at on audit_events(created_at);
create index ix_audit_events_resource on audit_events(resource_type,resource_id);
