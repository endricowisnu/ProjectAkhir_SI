package com.example.project_akhir_si.Component;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b7\u0018\u00002\u00020\u0001:\u0003\t\n\u000bB\u0017\b\u0004\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0005R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007\u0082\u0001\u0003\f\r\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/example/project_akhir_si/Component/BottomNavItem;", "", "route", "", "label", "(Ljava/lang/String;Ljava/lang/String;)V", "getLabel", "()Ljava/lang/String;", "getRoute", "Homecreen", "MaterialScreen", "ProfileScreen", "Lcom/example/project_akhir_si/Component/BottomNavItem$Homecreen;", "Lcom/example/project_akhir_si/Component/BottomNavItem$MaterialScreen;", "Lcom/example/project_akhir_si/Component/BottomNavItem$ProfileScreen;", "app_debug"})
public abstract class BottomNavItem {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String route = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String label = null;
    
    private BottomNavItem(java.lang.String route, java.lang.String label) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getLabel() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/project_akhir_si/Component/BottomNavItem$Homecreen;", "Lcom/example/project_akhir_si/Component/BottomNavItem;", "()V", "app_debug"})
    public static final class Homecreen extends com.example.project_akhir_si.Component.BottomNavItem {
        @org.jetbrains.annotations.NotNull
        public static final com.example.project_akhir_si.Component.BottomNavItem.Homecreen INSTANCE = null;
        
        private Homecreen() {
            super(null, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/project_akhir_si/Component/BottomNavItem$MaterialScreen;", "Lcom/example/project_akhir_si/Component/BottomNavItem;", "()V", "app_debug"})
    public static final class MaterialScreen extends com.example.project_akhir_si.Component.BottomNavItem {
        @org.jetbrains.annotations.NotNull
        public static final com.example.project_akhir_si.Component.BottomNavItem.MaterialScreen INSTANCE = null;
        
        private MaterialScreen() {
            super(null, null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/example/project_akhir_si/Component/BottomNavItem$ProfileScreen;", "Lcom/example/project_akhir_si/Component/BottomNavItem;", "()V", "app_debug"})
    public static final class ProfileScreen extends com.example.project_akhir_si.Component.BottomNavItem {
        @org.jetbrains.annotations.NotNull
        public static final com.example.project_akhir_si.Component.BottomNavItem.ProfileScreen INSTANCE = null;
        
        private ProfileScreen() {
            super(null, null);
        }
    }
}